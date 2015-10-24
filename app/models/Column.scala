package models

case class Column(
  name: Symbol,
  dbName: String,
  inserts: Boolean = true,
  updates: Boolean = true,
  generatedValue: Boolean = false
)
