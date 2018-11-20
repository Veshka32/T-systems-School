package model.entity;

import model.enums.RELATION;

import javax.persistence.*;

@Entity
@Table(name = "optionRelation")
@NamedQueries({
        @NamedQuery(name = "delete_incompatible", query = "delete from OptionRelation r where (r.one.id=:id or r.another.id=:id) and r.relation='INCOMPATIBLE'"),
        @NamedQuery(name = "delete_mandatory", query = "delete from OptionRelation r where r.one.id=:id  and r.relation='MANDATORY'"),
        @NamedQuery(name = "get_mandatory_for", query = "from OptionRelation r where  r.relation='MANDATORY' and r.one.id in (:ids)"),
        @NamedQuery(name = "get_incompatible_for", query = "from OptionRelation r where r.one.id in (:ids) and r.another.id in (:ids) and r.relation='INCOMPATIBLE'"),
        @NamedQuery(name = "get_mandatory_names", query = "select r.another.name from OptionRelation r where  r.relation='MANDATORY' and r.one.id=:id"),
        @NamedQuery(name = "get_mandatory_ids", query = "select r.another.id from OptionRelation r where r.relation='MANDATORY' and r.one.id in (:ids)"),
        @NamedQuery(name = "get_incompatible_names", query = "select r.another.name from OptionRelation  r where r.relation='INCOMPATIBLE' and r.one.id=:id"),
        @NamedQuery(name = "get_incompatible_names_1", query = "select r.one.name from OptionRelation  r where r.relation='INCOMPATIBLE' and r.another.id=:id"),
        @NamedQuery(name = "get_incompatible_ids", query = "select r.another.id from OptionRelation  r where r.relation='INCOMPATIBLE' and r.one.id in(:ids)"),
        @NamedQuery(name = "get_incompatible_ids_1", query = "select r.one.id from OptionRelation  r where r.relation='INCOMPATIBLE' and r.another.id in (:ids)"),
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
