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
@Table(name = "ciudad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ciudad.findAll", query = "SELECT c FROM Ciudad c"),
    @NamedQuery(name = "Ciudad.findByNumeroCiudad", query = "SELECT c FROM Ciudad c WHERE c.numeroCiudad = :numeroCiudad"),
    @NamedQuery(name = "Ciudad.findByNombreCiudad", query = "SELECT c FROM Ciudad c WHERE c.nombreCiudad = :nombreCiudad")})
public class Ciudad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "numero_ciudad")
    private Integer numeroCiudad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombre_ciudad")
    private String nombreCiudad;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ciudadNumeroCiudad", fetch = FetchType.LAZY)
    private Collection<Usuario> usuarioCollection;

    public Ciudad() {
    }

    public Ciudad(Integer numeroCiudad) {
        this.numeroCiudad = numeroCiudad;
    }

    public Ciudad(Integer numeroCiudad, String nombreCiudad) {
        this.numeroCiudad = numeroCiudad;
        this.nombreCiudad = nombreCiudad;
    }

    public Integer getNumeroCiudad() {
        return numeroCiudad;
    }

    public void setNumeroCiudad(Integer numeroCiudad) {
        this.numeroCiudad = numeroCiudad;
    }

    public String getNombreCiudad() {
        return nombreCiudad;
    }

    public void setNombreCiudad(String nombreCiudad) {
        this.nombreCiudad = nombreCiudad;
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
        hash += (numeroCiudad != null ? numeroCiudad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ciudad)) {
            return false;
        }
        Ciudad other = (Ciudad) object;
        if ((this.numeroCiudad == null && other.numeroCiudad != null) || (this.numeroCiudad != null && !this.numeroCiudad.equals(other.numeroCiudad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.barber.model.Ciudad[ numeroCiudad=" + numeroCiudad + " ]";
    }
    
}
