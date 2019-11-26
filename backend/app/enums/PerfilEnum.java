package enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum PerfilEnum {

	ANALISTA_CAR,
	ANALISTA_GEO,
	ANALISTA_TECNICO,
	DIRETOR,
	GERENTE,
	PRESIDENTE;

	public static List<String> getList() {

		return Stream.of(PerfilEnum.values())
				.map(PerfilEnum::name)
				.collect(Collectors.toList());

	}

	}
