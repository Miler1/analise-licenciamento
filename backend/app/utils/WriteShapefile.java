package utils;

import com.vividsolutions.jts.geom.Geometry;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

public class WriteShapefile {

    private static SimpleFeatureTypeBuilder typeBuilder() {

        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setName("MyFeatureType");
        typeBuilder.setCRS(DefaultGeographicCRS.WGS84);
        typeBuilder.srid(4674);
        return typeBuilder;

    }

    private static <T> List<SimpleFeature> toSimpleFeatures(List<T> geometries, BiConsumer<SimpleFeatureBuilder, T> fun,
                                                            SimpleFeatureBuilder featureBuilder) {

        AtomicInteger id = new AtomicInteger(0);
        return geometries.stream().map(g -> {
            fun.accept(featureBuilder, g);
            return featureBuilder.buildFeature(String.valueOf(id.getAndIncrement()));
        }).collect(toList());

    }

    private static  Map<String, Serializable> params(File file) throws MalformedURLException {

        Map<String, Serializable> params = new HashMap<>();
        params.put("url", file.toURI().toURL());
        params.put("create spatial index", Boolean.TRUE);
        return params;

    }

    private static <T> void write(File file, Consumer<SimpleFeatureTypeBuilder> fun1,
                                  BiConsumer<SimpleFeatureBuilder, T> fun2,
                                  List<T> geometries) throws IOException {

        SimpleFeatureTypeBuilder typeBuilder = typeBuilder();

        fun1.accept(typeBuilder);

        SimpleFeatureType type = typeBuilder.buildFeatureType();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);

        List<SimpleFeature> geoms = toSimpleFeatures(geometries, fun2, featureBuilder);

        Map<String, Serializable> params = params(file);

        ShapefileDataStoreFactory datastoreFactory = new ShapefileDataStoreFactory();
        ShapefileDataStore datastore = (ShapefileDataStore) datastoreFactory.createNewDataStore(params);
        datastore.createSchema(type);

        Transaction transaction = new DefaultTransaction("create");

        String typeName = datastore.getTypeNames()[0];
        SimpleFeatureSource featureSource = datastore.getFeatureSource(typeName);
        SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
        SimpleFeatureCollection collection = new ListFeatureCollection(type, geoms);

        featureStore.setTransaction(transaction);
        featureStore.addFeatures(collection);

        transaction.commit();
        transaction.close();
        datastore.dispose();

    }

    public static void write(File file, List<Geometry> geometries, Class<?> type) throws IOException {

        write(file, typeBuilder -> typeBuilder.add("the_geom", type), SimpleFeatureBuilder::add, geometries);

    }
}
