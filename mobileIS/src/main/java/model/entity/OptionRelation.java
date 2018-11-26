package model.entity;

import model.enums.RELATION;

import javax.persistence.*;

@Entity
@Table(name = "OptionRelation")
@NamedQueries({
        @NamedQuery(name = "delete_incompatible", query = "delete from OptionRelation r where (r.one.id=:id or r.another.id=:id) and r.relation='INCOMPATIBLE'"),
        @NamedQuery(name = "delete_mandatory", query = "delete from OptionRelation r where r.one.id=:id  and r.relation='MANDATORY'"),
        @NamedQuery(name = "get_mandatory_for", query = "from OptionRelation r where  r.relation='MANDATORY' and r.one.id in (:ids)"),
        @NamedQuery(name = "get_incompatible_for_in_range", query = "from OptionRelation r where (r.one.id in (:ids) and r.another.id in (:ids)) and r.relation='INCOMPATIBLE'"),
        @NamedQuery(name = "get_incompatible", query = "select r.one from OptionRelation r where r.relation='INCOMPATIBLE' and r.one.id in(:ids) and r.another.id in (:ids2)"),
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
