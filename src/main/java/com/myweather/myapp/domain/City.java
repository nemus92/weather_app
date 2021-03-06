package com.myweather.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A City.
 */
@Entity
@Table(name = "city")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class City implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "open_weather_id")
    private Integer openWeatherId;

    @OneToMany(mappedBy = "city")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Weather> ids = new HashSet<>();

    public City() {}

    public City(String name, Integer openWeatherId) {
        this.name = name;
        this.openWeatherId = openWeatherId;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public City name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOpenWeatherId() {
        return openWeatherId;
    }

    public City openWeatherId(Integer openWeatherId) {
        this.openWeatherId = openWeatherId;
        return this;
    }

    public void setOpenWeatherId(Integer openWeatherId) {
        this.openWeatherId = openWeatherId;
    }

    public Set<Weather> getIds() {
        return ids;
    }

    public City ids(Set<Weather> weathers) {
        this.ids = weathers;
        return this;
    }

    public City addId(Weather weather) {
        this.ids.add(weather);
        weather.setCity(this);
        return this;
    }

    public City removeId(Weather weather) {
        this.ids.remove(weather);
        weather.setCity(null);
        return this;
    }

    public void setIds(Set<Weather> weathers) {
        this.ids = weathers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof City)) {
            return false;
        }
        return id != null && id.equals(((City) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "City{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", openWeatherId=" + getOpenWeatherId() +
            "}";
    }
}
