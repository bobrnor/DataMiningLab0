package db;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: bobrnor
 * Date: 12/8/13
 * Time: 14:01
 * To change this template use File | Settings | File Templates.
 */
@javax.persistence.Table(name = "dialogs", schema = "", catalog = "dml")
@Entity
public class DBDialogsEntity {
    private int id;
    private int source_type;
    private Set<DBTagsEntity> tags;
    private List<DBMessagesEntity> messages;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @javax.persistence.Column(name = "id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @javax.persistence.Column(name = "source_type", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getSourceType() {
        return source_type;
    }

    public void setSourceType(int source_type) {
        this.source_type = source_type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DBDialogsEntity that = (DBDialogsEntity) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @javax.persistence.JoinTable(name = "dialogs_tags", catalog = "dml", schema = "", joinColumns = @javax.persistence.JoinColumn(name = "dialog_id", referencedColumnName = "id", nullable = false), inverseJoinColumns = @javax.persistence.JoinColumn(name = "tag_id", referencedColumnName = "id", nullable = false))
    @ManyToMany
    public Set<DBTagsEntity> getTags() {
        return tags;
    }

    public void setTags(Set<DBTagsEntity> tags) {
        this.tags = tags;
    }

    @OneToMany(mappedBy = "dialog")
    public List<DBMessagesEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<DBMessagesEntity> messages) {
        this.messages = messages;
    }
}
