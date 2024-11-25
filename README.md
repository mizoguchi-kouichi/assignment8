# StudentManagementAPI

***

## 概要

***

- このAPIは、中学校および高校の学生データを管理するためのCRUD処理を実装しています。

## 開発環境

***

- 言語 ：Java17  
  この言語選んだきっかけは、今回始めてのアプリ開発において古くから使われており  
  現在も広く使用されている言語だからです。  
  また、Javaは正確にコード記述しないと動かないため、メソッドや変数の動作を  
  理解する力が身に付くと思いました。  
  <br/>

- フレームワーク ：Spring Boot 3.2.3  
  このフレームワークを選んだ理由は、必要なライブラリを自動的にまとめてインストールしてくれること、  
  最小限の設定で開発を始められること、そしてTomcatサーバーが組み込まれているからです。  
  <br/>

- データベース :MySQL  
  このデータベースを選んだ理由は、広く使用されているオープンソースのデータベースであり、  
  Spring Bootでのデータベース接続の設定が簡単で、データソースの設定が自動的に行われるからです。  
  <br/>

- テスト : JUnit ・Mockito ・AssertJ ・Database Rider
    - JUnitを選んだ理由は、テストコードを自動的に実行でき、CI/CDパイプラインとの統合が容易だからです。  
      <br/>
    - Mockitoを選んだ理由は、Serviceの単体テストにおいて、依存するデータベースやクラスをモック化し、  
      それらのレスポンスの振る舞いを詳細に定義できるからです。  
      <br/>
    - AssertJを選んだ理由は、JUnitでのテストにおいて、より多様な検証条件を使用できるからです。  
      <br/>
    - Database Riderを選んだ理由は、MapperのDBテストとControllerの結合テストにおいて、  
      初期のデータベース状態とメソッド実行後のデータベース状態を簡単に検証できるからです  
      <br/>

- バージョン管理 :Git ・GitHub
    - Gitを選んだ理由は、ブランチ機能により複数開発が出来る点やコードの変更履歴を追跡できる点で選びました。  
      <br/>
    - GitHubを選んだ理由は、現代のソフトウェア開発において最も一般的で信頼性の高いバージョン管理システムであり、  
      プルリクエストによるコードレビューが可能だからです。  
      また、GitHub Actionsを使用することでCI・CDの実装を容易に構築できるからです。
- サーバー :AWS
    - AWSを選んだ理由は、現在最も広く使用されているクラウドプラットフォームであるからです。  
      また、EC2やRDSなどの多様なサービスから必要なものを選択してサーバー環境を構築できる柔軟性があるためです。

## 使用ツール

***

- IntelliJ
    - IntelliJを選んだ理由は、現在の開発環境で最も広く使用されているIDEだからです。  
      また、特にJava開発において主流のIDEであり、Spring Bootの機能を充実したサポートし、  
      優れたデバッグ機能を提供しているからです。
- Docker
- Docker Compose
- Postman

## データベース概要

***

| カラム名(論理名) | カラム名(物理名)   | 型・桁         | Nullable | その他コメント                               | 
|-----------|-------------|-------------|----------|---------------------------------------|
| ID        | id          | int         | No       | PRIMARY KEY・ UNSIGNED・ AUTO_INCREMENT |
| 名前        | name        | VARCHAR(20) | Yes      |                                       |
| 学年        | grade       | VARCHAR(20) | Yes      |                                       |
| 出身地       | birth_place | VARCHAR(20) | Yes      |                                       |

## 実装機能

***

### 実装機能の種類

| No | CRUD   | エンドポイント                            | 機能        | 機能について              | その他のコメント                          |
|----|--------|------------------------------------|-----------|---------------------|-----------------------------------|
| ①  | Read   | GET /students/{id}                 | 学生ID参照    | 指定したIDの学生のデータを参照します |                                   |
| ②  | Read   | GET students                       | 全学生参照     | 全学生のデータを参照します       | クエリ文字列で学年・頭文字・出身地を指定して参照することも出来ます |
| ③  | Create | POST /students                     | 新規学生登録    | 新しい学生を登録します         |                                   |
| ④  | Update | PATCH /students/{id}               | 学生IDデータ更新 | 指定した学生のデータを更新します    |                                   |
| ⑤  | update | PATCH /students/grade/_batchUpdate | 全学生学年更新   | 全学生の学年を一斉に更新します     |                                   |
| ⑥  | delete | DELETE /students/{id}              | 学生ID削除    | 指定したIDの学生のデータを削除します |                                   |

### API仕様書

***
APIの詳細な仕様については、以下のSwagger UIを参照してください。

[API仕様書](https://mizoguchi-kouichi.github.io/assignment8/)

## レスポンスハンドリング

***

このAPIは、以下のようなエラー状況に対してレスポンスを返します。

| ステータスコード                   | 説明                   | 状態  |
|----------------------------|----------------------|-----|
| 200  OK                    | リクエストが正常に処理できた       | 正常  |
| 201  Created               | リクエストが成功してリソースの作成が完了 | 正常  |
| 400  Bad Request           | 一般的なクライアントエラー        | エラー |
| 404  Not Found             | Webページが見つからない        | エラー |
| 500  Internal Server Error | 何らかのサーバ内で起きたエラー      | エラー |

## AWS構成図

![StudentManagementAWS構成図.drawio (1).png](..%2F..%2F..%2FDownloads%2FStudentManagementAWS%E6%A7%8B%E6%88%90%E5%9B%B3.drawio%20%281%29.png)

## デプロイ状況

- Postmanで確認しました。

## 工夫した点

-
リクエストの型の間違いはController層でエラーレスポンスを返し、データベースにデータが存在しない場合や、登録・更新時のカラム入力の間違い等の  
アプリケーション内のエラーは、Service層でエラーレスポンスを返すように工夫しました。

- 各メソッドにJavaDocによる説明文を追加しました。

## 課題になった点

- バリデーションエラーレスポンスボディを作成で自分がしたいレスポンスボディに作成出来ず悩みました。
- テストコードの効率化のため、@ParameterizedTestと@CsvSourceを活用したアプローチを試みました。  
  参考資料が限られていましたが、JUnit公式ドキュメントを詳細に研究することで、最終的に実装に成功しました。
- API仕様書の実装過程で、不明点に遭遇した際、ChatGPTや技術記事での調査が多くなっていました。  
  より効率的な解決のためには、早期に質問や相談することの大切さを知りました。

## 今後の展望

- フロント側の実装