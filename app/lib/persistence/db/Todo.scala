/**
 *
 * to do sample project
 *
 */

package lib.persistence.db

import java.time.LocalDateTime
import slick.jdbc.JdbcProfile
import ixias.persistence.model.Table

import lib.model.Todo
import lib.model.Category

case class TodoTable[P <: JdbcProfile]()(implicit val driver: P)
  extends Table[Todo, P] with SlickColumnTypes[P] {
  
  import api._

  // --[ declare table ] ----------------------------------------------
  lazy val dsn = Map(
    "master" -> DataSourceName("ixias.db.mysql://master/to-do-sample"),
    "slave"  -> DataSourceName("ixias.db.mysql://slave/to-do-sample")
  )

  // --[ declare query ] ----------------------------------------------
  class Query extends BasicQuery(new Table(_)) {}
  lazy val query = new Query

  // --[ declare all column ] ----------------------------------------------
  
  class Table(tag: Tag) extends BasicTable(tag, "to-do") {
    // Columns
    /* @1 */ def id        = column[Todo.Id]       ("id",          O.UInt64, O.PrimaryKey, O.AutoInc)
    /* @2 */ def cid       = column[Category.Id]   ("category_id", O.UInt64)
    /* @3 */ def title     = column[String]        ("title",       O.Utf8Char255)
    /* @3 */ def body      = column[String]        ("body",        O.Utf8Char255)
    /* @4 */ def updatedAt = column[LocalDateTime] ("update_at",   O.TsCurrent)
    /* @5 */ def createdAt = column[LocalDateTime] ("created_at",  O.Ts)

    // All columns as a tuple
    type TableElementTuple = (
      Option[Todo.Id], Category.Id, String, String, LocalDateTime, LocalDateTime
    )
  
    // The * projection of the table
    def * = (id.?, cid, title, body, updatedAt, createdAt) <> (
      /** The bidirectional mappings : Tuple(table) => Model */
      (t: TableElementTuple) => Todo(
        t._1, t._2, t._3, t._4, t._5, t._6
      ),
      /** The bidirectional mappings : Model => Tuple(table) */
      (v: TableElementType) => Todo.unapply(v).map { t => (
        t._1, t._2, t._3, t._4, LocalDateTime.now(), t._5
      )}
    )
  }
}