package model.entity;
/**
 * This class represent logic relation between to {@code Option} entity.
 * Relation can be non-direct (like INCOMPATIBLE) or direct (like MANDATORY), but can not be self-referenced.
 * In case of MANDATORY relation, OptionRelation.one requires OptionRelation.another.
 * Each option can be part of many OptionRelation.
 */

import model.enums.RELATION;

import javax.persistence.*;

@Entity
@Table(name = "optionrelation")
@NamedQueries({
        @NamedQuery(name = "delete_incompatible",
                query = "delete from OptionRelation r where (r.one.id=:id or r.another.id=:id) and r.relation='INCOMPATIBLE'"),
        @NamedQuery(name = "delete_mandatory",
                query = "delete from OptionRelation r where r.one.id=:id  and r.relation='MANDATORY'"),
        @NamedQuery(name = "get_mandatory_for",
                query = "from OptionRelation r where  r.relation='MANDATORY' and r.one.id in (:ids)"),
        @NamedQuery(name = "get_incompatible_for",
                query = "from OptionRelation r where (r.one.id =:id or r.another.id =:id) and r.relation='INCOMPATIBLE'"),
        @NamedQuery(name = "get_mutual_incompatible",
                query = "from OptionRelation r where r.one.id in (:ids) and r.another.id in (:ids) and r.relation='INCOMPATIBLE'"),
        @NamedQuery(name = "get_incompatible",
                query = "select r.one from OptionRelation r where r.relation='INCOMPATIBLE' and r.one.id in(:ids) and r.another.id in (:ids2)"),
        @NamedQuery(name = "get_incompatible_another",
                query = "select r.another from OptionRelation r where r.relation='INCOMPATIBLE' and r.another.id in(:ids) and r.one.id in (:ids2)"),
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

    public OptionRelation() {
        //no arg constructor
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OptionRelation)) return false;
        return id == (((OptionRelation) o).id);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return one + " to " + another + ": " + relation;
    }

    public Option getOne() {
        return this.one;
    }

    public void setOne(Option one) {
        this.one = one;
    }

    public Option getAnother() {
        return this.another;
    }

    public void setAnother(Option another) {
        this.another = another;
    }

    public RELATION getRelation() {
        return this.relation;
    }

    public void setRelation(RELATION relation) {
        this.relation = relation;
    }
}
