package enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum TipoSobreposicaoDistanciaEnum {

	TERRA_INDIGENA_ZA,
	UC_FEDERAL_ZA,
	UC_ESTADUAL_ZA;

	public static List<String> getList() {

		return Stream.of(TipoSobreposicaoDistanciaEnum.values())
				.map(TipoSobreposicaoDistanciaEnum::name)
				.collect(Collectors.toList());

	}

}
