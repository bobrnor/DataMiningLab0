package db;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: bobrnor
 * Date: 12/8/13
 * Time: 14:01
 * To change this template use File | Settings | File Templates.
 */
@javax.persistence.Table(name = "users", schema = "", catalog = "dml")
@Entity
public class DBUsersEntity {
    private int id;
    private int source_type;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @javax.persistence.Column(name = "id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private long iid;

    @javax.persistence.Column(name = "iid", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public long getIid() {
        return iid;
    }

    public void setIid(long iid) {
        this.iid = iid;
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

        DBUsersEntity that = (DBUsersEntity) o;

        if (id != that.id) return false;
        if (iid != that.iid) return false;
        if (source_type != that.source_type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + source_type;
        result = 31 * result + (int) (iid ^ (iid >>> 32));
        return result;
    }
}
