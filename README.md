# APIの概要

***

- このAPIは、中学校および高校の学生データを管理するためのCRUD処理を実装しています。

# データベース概要

***

| カラム名(論理名) | カラム名(物理名)   | 型・桁         | Nullable | その他コメント                               | 
|-----------|-------------|-------------|----------|---------------------------------------|
| ID        | id          | int         | No       | PRIMARY KEY・ UNSIGNED・ AUTO_INCREMENT |
| 名前        | name        | VARCHAR(20) | Yes      |                                       |
| 学年        | grade       | VARCHAR(20) | Yes      |                                       |
| 出身地       | birth_place | VARCHAR(20) | Yes      |                                       |

# API仕様書

APIの詳細な仕様については、以下のSwagger UIを参照してください。

[Swagger UI](http://localhost:8080/swagger-ui.html)
