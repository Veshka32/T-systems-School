package entities;

import entities.enums.RELATION;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "optionRelation")
@NamedQueries({
        @NamedQuery(name = "delete_incompatible", query = "delete from OptionRelation r where (r.one.id=:id or r.another.id=:id) and r.relation='INCOMPATIBLE'"),
        @NamedQuery(name = "delete_mandatory", query = "delete from OptionRelation r where r.one.id=:id  and r.relation='MANDATORY'"),
        @NamedQuery(name = "get_all_mandatory_names", query = "select r.another.name from OptionRelation r where  r.relation='MANDATORY' and r.one.name in (:names)"),
        @NamedQuery(name = "get_all_incompatible_names", query = "select r.another.name from OptionRelation  r where r.relation='INCOMPATIBLE' and r.one.name in (:names)"),
}
)
public class OptionRelation extends AbstractEntity {

    @OneToOne
    private Option one;

    @OneToOne
    private Option another;

    @Enumerated(EnumType.STRING)
    private RELATION relation;

    public OptionRelation(Option o, Option o1, RELATION r) {
        one = o;
        another = o1;
        relation = r;
    }

    public void setIncompatible(Option o, Option o1) {
        one = o;
        another = o1;
        relation = RELATION.INCOMPATIBLE;
    }

    public void setMandatory(Option o, Option o1) {
        one = o;
        another = o1;
        relation = RELATION.MANDATORY;
    }

}
