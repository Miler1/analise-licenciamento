package enums;

import models.tmsmap.MapaImagem;
import java.awt.*;
import java.util.stream.Stream;

public enum CorRestricaoEnum {

    TERRA_INDIGENA("Terra indígena", Color.decode("#845050")),
    UC_FEDERAL("Unidade de conservação federal", Color.decode("#ffaa4d")),
    UC_ESTADUAL("Unidade de conservação estadual", Color.decode("#ffaa4d")),
    UC_MUNICIPAL("Unidade de conservação municipal", Color.decode("#414141")),
    TERRA_INDIGENA_ZA("Terra indígena ZA", Color.decode("#996d6d")),
    TERRA_INDIGENA_ESTUDO("Terra indígena estudo", Color.decode("#FF1300")),
    UC_FEDERAL_APA_DENTRO("Unidade de conservação dentro APA", Color.decode("#414141")),
    UC_FEDERAL_APA_FORA("Unidade de conservação fora APA", Color.decode("#414141")),
    UC_FEDERAL_ZA("Unidade de conservação federal ZA", Color.decode("#414141")),
    UC_ESTADUAL_PI_DENTRO("Unidade de conservação estadual dentro PI", Color.decode("#e779c2")),
    UC_ESTADUAL_PI_FORA("Unidade de conservação estadual fora PI", Color.decode("#855b77")),
    UC_ESTADUAL_ZA("Unidade de conservação estadual ZA", Color.decode("#e28552")),
    TOMBAMENTO_ENCONTRO_AGUAS("Tombamento encontro das águas", Color.decode("#ff4c4c")),
    TOMBAMENTO_ENCONTRO_AGUAS_ZA("Tombamento encontro das águas ZA", Color.decode("#bb4c4c")),
    AREAS_EMBARGADAS_IBAMA("Áreas embargadas IBAMA", Color.decode("#be5dff")),
    AUTO_DE_INFRACAO_IBAMA("Auto de infração IBAMA", Color.decode("#502274")),
    SAUIM_DE_COLEIRA("Sauim de coleira", Color.decode("#502274")),
    SITIOS_ARQUEOLOGICOS("Sítios arqueológicos", Color.decode("#502274")),
    BENS_ACAUTELADOS_IPHAN_PT("Bens_Acautelados IPHAN Ponto", Color.decode("#502274")),
    BENS_ACAUTELADOS_IPHAN_POL("Bens_Acautelados IPHAN Poligono", Color.decode("#ff4c4c")),
    UC_ESTADUAL_ZA_PI_FORA("Unidade de conservação estadual ZA fora PI", Color.decode("#39FF14"));

    public String nomeRestricao;
    public Color codigoCor;

    CorRestricaoEnum(String nomeRestricao, Color codigoCor) {

        this.nomeRestricao = nomeRestricao;
        this.codigoCor = codigoCor;
    }

    public static Color getCorPeloNomeRestricao(String nome) {

        String colorCode = MapaImagem.getColorTemaCiclo();
        Color color = Color.decode(colorCode);
        Color fillColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 127);

        return Stream.of(CorRestricaoEnum.values()).filter(restricao -> restricao.nomeRestricao.equals(nome)).map(CorRestricaoEnum::getCodigoCor).findAny().orElse(fillColor);

    }

    public Color getCodigoCor() {
        return codigoCor;
    }

}
