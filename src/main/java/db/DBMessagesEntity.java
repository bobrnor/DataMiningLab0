package db;

import org.hibernate.annotations.SQLInsert;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: bobrnor
 * Date: 12/8/13
 * Time: 14:01
 * To change this template use File | Settings | File Templates.
 */
@SQLInsert(sql="insert ignore into dml.messages (author_id, body, date, dialog_id, fixed_body) values (?, ?, ?, ?, ?)")
@javax.persistence.Table(name = "messages", schema = "", catalog = "dml")
@Entity
public class DBMessagesEntity {
    private int id;
    private String body;
    private String fixedBody;
    private Timestamp date;
    private DBUsersEntity author;
    private DBDialogsEntity dialog;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @javax.persistence.Column(name = "id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @javax.persistence.Column(name = "body", columnDefinition="TEXT", nullable = true, insertable = true, updatable = true, precision = 0)
    @Basic
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @javax.persistence.Column(name = "fixed_body", columnDefinition="TEXT", nullable = true, insertable = true, updatable = true, precision = 0)
    @Basic
    public String getFixedBody() {
        return fixedBody;
    }

    public void setFixedBody(String fixedBody) {
        this.fixedBody = fixedBody;
    }

    @javax.persistence.Column(name = "date", nullable = false, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DBMessagesEntity that = (DBMessagesEntity) o;

        if (id != that.id) return false;
        if (body != null ? !body.equals(that.body) : that.body != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (fixedBody != null ? !fixedBody.equals(that.fixedBody) : that.fixedBody != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (fixedBody != null ? fixedBody.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @OneToOne
    @javax.persistence.JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    public DBUsersEntity getAuthor() {
        return author;
    }

    public void setAuthor(DBUsersEntity author) {
        this.author = author;
    }

    @ManyToOne
    @JoinColumn(name = "dialog_id", referencedColumnName = "id", nullable = false)
    public DBDialogsEntity getDialog() {
        return dialog;
    }

    public void setDialog(DBDialogsEntity dialog) {
        this.dialog = dialog;
    }
}
