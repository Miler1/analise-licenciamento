/**
 * Define the filters used on Hibernate queries on the entities of this package
 */
@FilterDefs(value = {
	@FilterDef( name = "ativoLicenciamento", parameters = @ParamDef(name = "ativo", type = "boolean"), defaultCondition = "ativo = :ativo" )
})
package models;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.ParamDef;
