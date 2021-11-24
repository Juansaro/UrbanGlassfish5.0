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
@Table(name = "tipo_telefono")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoTelefono.findAll", query = "SELECT t FROM TipoTelefono t"),
    @NamedQuery(name = "TipoTelefono.findByNumeroTipoTelefono", query = "SELECT t FROM TipoTelefono t WHERE t.numeroTipoTelefono = :numeroTipoTelefono"),
    @NamedQuery(name = "TipoTelefono.findByDescripcion", query = "SELECT t FROM TipoTelefono t WHERE t.descripcion = :descripcion")})
public class TipoTelefono implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "numero_tipo_telefono")
    private Integer numeroTipoTelefono;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoTelefonoNumeroTipoTelefono", fetch = FetchType.LAZY)
    private Collection<Usuario> usuarioCollection;

    public TipoTelefono() {
    }

    public TipoTelefono(Integer numeroTipoTelefono) {
        this.numeroTipoTelefono = numeroTipoTelefono;
    }

    public TipoTelefono(Integer numeroTipoTelefono, String descripcion) {
        this.numeroTipoTelefono = numeroTipoTelefono;
        this.descripcion = descripcion;
    }

    public Integer getNumeroTipoTelefono() {
        return numeroTipoTelefono;
    }

    public void setNumeroTipoTelefono(Integer numeroTipoTelefono) {
        this.numeroTipoTelefono = numeroTipoTelefono;
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
        hash += (numeroTipoTelefono != null ? numeroTipoTelefono.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoTelefono)) {
            return false;
        }
        TipoTelefono other = (TipoTelefono) object;
        if ((this.numeroTipoTelefono == null && other.numeroTipoTelefono != null) || (this.numeroTipoTelefono != null && !this.numeroTipoTelefono.equals(other.numeroTipoTelefono))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.barber.model.TipoTelefono[ numeroTipoTelefono=" + numeroTipoTelefono + " ]";
    }
    
}
