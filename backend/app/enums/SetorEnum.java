package enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum SetorEnum {

	GFAU,
	GCAP,
	GECF,
	GELI,
	GGEO,
	GEFA,
	GECP,
	NEAM,
	GERH,
	GERM;

	public static List<String> getList() {

		return Stream.of(SetorEnum.values())
				.map(SetorEnum::name)
				.collect(Collectors.toList());

	}

}
