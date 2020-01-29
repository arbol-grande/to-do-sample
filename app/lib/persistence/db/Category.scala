/**
 *
 * to do sample project
 *
 */

package lib.persistence.db

import java.time.LocalDateTime
import slick.jdbc.JdbcProfile
import ixias.persistence.model.Table

import lib.model.Category

case class CategoryTable[P <: JdbcProfile]()(implicit val driver: P)
  extends Table[Category, P] with SlickColumnTypes[P] {
  
  import api._

  // --[ declare table ] ----------------------------------------------
  lazy val dsn = Map(
    "master" -> DataSourceName("ixias.db.mysql://master/to_do_sample"),
    "slave"  -> DataSourceName("ixias.db.mysql://slave/to_do_sample")
  )

  // --[ declare query ] ----------------------------------------------
  class Query extends BasicQuery(new Table(_)) {}
  lazy val query = new Query

  // --[ declare all column ] ----------------------------------------------
  
  class Table(tag: Tag) extends BasicTable(tag, "to_do_category") {
    // Columns
    /* @1 */ def id            = column[Category.Id]            ("id",             O.UInt64, O.PrimaryKey, O.AutoInc)
    /* @2 */ def name          = column[String]                 ("name",           O.Utf8Char255)
    /* @3 */ def slug          = column[String]                 ("slug",           O.Utf8Char255)
    /* @4 */ def categoryColor = column[Category.CategoryColor] ("category_color", O.UInt8)
    /* @5 */ def updatedAt     = column[LocalDateTime]          ("updated_at",     O.TsCurrent)
    /* @6 */ def createdAt     = column[LocalDateTime]          ("created_at",     O.Ts)

    // All columns as a tuple
    type TableElementTuple = (
      Option[Category.Id], String, String, Category.CategoryColor, LocalDateTime, LocalDateTime
    )
  
    // The * projection of the table
    def * = (id.?, name, slug, categoryColor, updatedAt, createdAt) <> (
      /** The bidirectional mappings : Tuple(table) => Model */
      (t: TableElementTuple) => Category(
        t._1, t._2, t._3, t._4, t._5, t._6
      ),
      /** The bidirectional mappings : Model => Tuple(table) */
      (v: TableElementType) => Category.unapply(v).map { t => (
        t._1, t._2, t._3, t._4, LocalDateTime.now(), t._6
      )}
    )
  }
}
