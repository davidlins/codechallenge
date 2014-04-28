package codechallenge.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Stations")
public class Station implements Serializable {

   private static final long serialVersionUID = -251639439130790444L;

   @Id
   @NotNull
   private Integer id;
   private String name;

   public Integer getId() {
      return this.id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }
}