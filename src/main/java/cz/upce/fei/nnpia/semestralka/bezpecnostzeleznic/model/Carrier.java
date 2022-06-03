package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class Carrier {
    private Long id;

    private String name;

    public Carrier() {
    }

    public Carrier(String name) {
        this();
        this.name = name;
    }
}
