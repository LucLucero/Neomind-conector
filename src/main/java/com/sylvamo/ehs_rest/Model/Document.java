package com.sylvamo.ehs_rest.Model;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "V_DOCS_NEOMIND")
@Immutable
public class Document {

    @Id
    @Column(name = "NEOID")
    private String neoid;

    @Column(name = "NOMEDOCUMENTO")
    private String nomeDocumento;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "DATAUPDATE")
    private Timestamp dataupdate;

    @Column(name = "CODE")
    private String code;

    @Column(name = "RN")
    private Integer rn;

    public String getNeoid() { return neoid; }
    public String getNomeDocumento() { return nomeDocumento; }
    public String getNome() { return nome; }
    public Timestamp getDataupdate() { return dataupdate; }
    public String getCode() { return code; }
    public Integer getRn() { return rn; }
}
