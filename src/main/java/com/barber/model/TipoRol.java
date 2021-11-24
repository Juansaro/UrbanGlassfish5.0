/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barber.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author juan
 */
@Entity
@Table(name = "tipo_rol")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoRol.findAll", query = "SELECT t FROM TipoRol t"),
    @NamedQuery(name = "TipoRol.findByNumeroRol", query = "SELECT t FROM TipoRol t WHERE t.numeroRol = :numeroRol"),
    @NamedQuery(name = "TipoRol.findByDescripcion", query = "SELECT t FROM TipoRol t WHERE t.descripcion = :descripcion")})
public class TipoRol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "numero_rol")
    private Integer numeroRol;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoRolNumeroRol", fetch = FetchType.LAZY)
    private Collection<Usuario> usuarioCollection;

    public TipoRol() {
    }

    public TipoRol(Integer numeroRol) {
        this.numeroRol = numeroRol;
    }

    public TipoRol(Integer numeroRol, String descripcion) {
        this.numeroRol = numeroRol;
        this.descripcion = descripcion;
    }

    public Integer getNumeroRol() {
        return numeroRol;
    }

    public void setNumeroRol(Integer numeroRol) {
        this.numeroRol = numeroRol;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public Collection<Usuario> getUsuarioCollection() {
        return usuarioCollection;
    }

    public void setUsuarioCollection(Collection<Usuario> usuarioCollection) {
        this.usuarioCollection = usuarioCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numeroRol != null ? numeroRol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoRol)) {
            return false;
        }
        TipoRol other = (TipoRol) object;
        if ((this.numeroRol == null && other.numeroRol != null) || (this.numeroRol != null && !this.numeroRol.equals(other.numeroRol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descripcion;
    }
    
}
