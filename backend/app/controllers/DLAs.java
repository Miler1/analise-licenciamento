package controllers;
import models.licenciamento.DispensaLicenciamento;
import serializers.DLASerializer;

public class DLAs extends InternalController{

	public static void findById(Long idDLA) {

		returnIfNull(idDLA, "Long");

		DispensaLicenciamento dispensa = DispensaLicenciamento.findById(idDLA);

		renderJSON(dispensa, DLASerializer.list);

	}
}
