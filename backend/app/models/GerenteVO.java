package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class GerenteVO {

    @Id
    @Column(name="id_usuario")
    public Long id;

    @Column(name="count")
    public Long numeroProcessos;
}