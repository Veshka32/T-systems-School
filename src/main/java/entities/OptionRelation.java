package entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@NamedQueries({
        @NamedQuery(name = "delete_incompatible", query = "delete from OptionRelation r where (r.one.id=:id or r.another.id=:id) and r.relation='INCOMPATIBLE'"),
        @NamedQuery(name = "delete_mandatory", query = "delete from OptionRelation r where r.one.id=:id  and r.relation='MANDATORY'"),
        @NamedQuery(name = "get_all_mandatory_names", query = "select r.another.name from OptionRelation r where  r.relation='MANDATORY' and r.one.name in (:names)"),
        @NamedQuery(name = "get_all_incompatible_names", query = "select r.another.name from OptionRelation  r where r.relation='INCOMPATIBLE' and r.one.name in (:names)"),

}
)
public class OptionRelation extends AbstractEntity {

    @ManyToOne
    private TariffOption one;

    @ManyToOne
    private TariffOption another;

    @Enumerated(EnumType.STRING)
    private RELATION relation;

    public OptionRelation(TariffOption o, TariffOption o1, RELATION r) {
        one = o;
        another = o1;
        relation = r;
    }

    public void setIncompatible(TariffOption o, TariffOption o1) {
        one = o;
        another = o1;
        relation = RELATION.INCOMPATIBLE;
    }

    public void setMandatory(TariffOption o, TariffOption o1) {
        one = o;
        another = o1;
        relation = RELATION.MANDATORY;
    }

}
