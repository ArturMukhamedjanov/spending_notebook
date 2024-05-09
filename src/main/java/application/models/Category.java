package application.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "category_relations",
        joinColumns = @JoinColumn(name = "child_id"),
        inverseJoinColumns = @JoinColumn(name = "parent_id")
    )
    private List<Category> parents = new ArrayList<>();

    @ManyToMany(mappedBy = "parents", fetch = FetchType.EAGER)
    private List<Category> children = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.EAGER)
    private List<Mcc> mccs = new ArrayList<>();

    public void addChild(Category child) {
        children.add(child);
        child.addParent(this);
    }

    public void removeChild(Category child) {
        children.remove(child);
        child.removeParent(this);
    }

    public void removeAllChildren() {
        for (Category child : children) {
            child.removeParent(this);
        }
        children.clear();
    }

    public void addParent(Category parent){
        parents.add(parent);
    }

    public void removeParent(Category parnet){
        parents.remove(parnet);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" [");
        for (Mcc mcc : mccs) {
            sb.append(mcc.getCode()).append(", ");
        }
        if (!mccs.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length()); // Удаляем последнюю запятую и пробел
        }
        sb.append("]");
        return sb.toString();
    }

}