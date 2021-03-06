# to-do-sample

## 環境構築

このプロジェクトを動かすためにわざわざ自分のmysql環境汚すのはあれなのでdockerで仮想環境を作る。

```
$ pwd                   // 今のディレクトリを確認
xx/xx/xx/to-do-sample   // プロジェクト直下であることを確認

$ docker-compose up -d  // docker上でこのプロジェクト用のmysqlを立てる

// ちょっと待機
```

#### mysqlにlogin

```
$ mysql -udocker -h127.0.0.1 -P33306 -p // dockerで立てたmysqlサーバーにログイン
Password: { docker }                    // passはdocker

/**** ログイン成功 ****/

mysql> Show DATABASES;                  // データベース一覧

+--------------------+                  // to_do_sample っていうDBがあることを確認
| Database           |
+--------------------+
| information_schema |
| to_do_sample       |
+--------------------+

```

#### mysqlにサンプルデータを挿入

```
/**** テーブルが空なことを確認 ****/
mysql> SHOW TABLES;
+------------------------+
| Tables_in_to_do_sample |
+------------------------+
0 rows in set

/**** 'to_do_category'テーブル作成 ****/

musql> CREATE TABLE `to_do_category` (
         `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
         `name` VARCHAR(255) NOT NULL,
         `slug` VARCHAR(255) NOT NULL,
         `category_color` TINYINT NOT NULL,
         `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
         `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
         PRIMARY KEY (`id`)
       ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**** 'to_do_category'テーブルが作られたことを確認 ****/

mysql> SHOW TABLES;
+------------------------+
| Tables_in_to_do_sample |
+------------------------+
| to_do_category         |
+------------------------+
1 row in set

/**** 'to_do_category'テーブルのサンプルデータ挿入 ****/

mysql> INSERT INTO to_do_category(name,slug,category_color) values('フロントエンド','front',1);
mysql> INSERT INTO to_do_category(name,slug,category_color) values('バックエンド','back',2);
mysql> INSERT INTO to_do_category(name,slug,category_color) values('インフラ','infra',3);

/*** データが入っているか確認 ****/

mysql> SELECT * FROM to_do_category;
+----+----------------+-------+----------------+---------------------+---------------------+
| id | name           | slug  | category_color | updated_at          | created_at          |
+----+----------------+-------+----------------+---------------------+---------------------+
| 1  | フロントエンド | front | 1              | 2020-01-31 17:29:38 | 2020-01-31 17:29:38 |
| 2  | バックエンド   | back  | 2              | 2020-01-31 17:29:38 | 2020-01-31 17:29:38 |
| 3  | インフラ       | infra | 3              | 2020-01-31 17:29:38 | 2020-01-31 17:29:38 |
+----+----------------+-------+----------------+---------------------+---------------------+

/**** 'to_do'テーブルを作成 ****/

mysql> CREATE TABLE `to_do` (
         `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
         `category_id` bigint(20) unsigned NOT NULL,
         `title` VARCHAR(255) NOT NULL,
         `body` VARCHAR(255) NOT NULL,
         `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
         `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
         PRIMARY KEY (`id`)
       ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**** 'to_do'テーブルが作られたことを確認 ****/

mysql> SHOW TABLES;
+------------------------+
| Tables_in_to_do_sample |
+------------------------+
| to_do                  |
| to_do_category         |
+------------------------+
2 rows in set


/**** 'to_do'テーブルのサンプルデータ挿入 ****/

mysql> INSERT INTO `to_do`(category_id,title,body) values(1, 'デザインをいい感じにする','ヘッダーのデザインをもっといい感じに');
mysql> INSERT INTO `to_do`(category_id,title,body) values(2, 'Controllerの修正','Controller名をもっといい感じに');
mysql> INSERT INTO `to_do`(category_id,title,body) values(3, '新しいDB環境の作成','タイトル通り');

/*** データが入っているか確認 ****/

mysql> SELECT * FROM to_do;
+----+-------------+--------------------------+--------------------------------------+---------------------+---------------------+
| id | category_id | title                    | body                                 | updated_at          | created_at          |
+----+-------------+--------------------------+--------------------------------------+---------------------+---------------------+
| 1  | 1           | デザインをいい感じにする | ヘッダーのデザインをもっといい感じに | 2020-02-21 17:53:22 | 2020-02-21 17:53:22 |
| 2  | 2           | Controllerの修正         | Controller名をもっといい感じに       | 2020-02-21 17:53:22 | 2020-02-21 17:53:22 |
| 3  | 3           | 新しいDB環境の作成       | タイトル通り                         | 2020-02-21 17:53:22 | 2020-02-21 17:53:22 |
+----+-------------+--------------------------+--------------------------------------+---------------------+---------------------+

/**** 完了 ****/
```

### playframeworkを起動

```
$ sbt run   // サーバーが起動したらlocalhost:9000にアクセス
```
