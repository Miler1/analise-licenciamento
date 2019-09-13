package enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ComunicadoOrgaoEnum {

	TOMBAMENTO_ENCONTRO_AGUAS,
	TOMBAMENTO_ENCONTRO_AGUAS_ZA,
	AREAS_EMBARGADAS_IBAMA,
	AUTO_DE_INFRACAO_IBAMA,
	SAUIM_DE_COLEIRA,
	SITIOS_ARQUEOLOGICOS;

	public static List<String> getList() {

		return Stream.of(ComunicadoOrgaoEnum.values())
				.map(ComunicadoOrgaoEnum::name)
				.collect(Collectors.toList());

	}

}
