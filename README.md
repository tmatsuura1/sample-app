## アプリ実行手順

### 前提条件
- Docker Desktopがインストールされていること
- Quarkus CLI（`quarkus`コマンド）が使用できる状態になっていること（https://ja.quarkus.io/guides/cli-tooling.html）

### 1. バックエンドのサーバーを起動する
```bash
cd backend/
quarkus dev
```

### 2. フロントエンドのサーバーを起動する
```bash
cd frontend/
cp .env.example .env
npm install
npm run dev
```

### 3. ユーザー検索画面へアクセスする
npm run dev実行後に表示されるURLに「/users」を追加したURLにブラウザでアクセスする。検索画面から詳細画面/登録画面に遷移できる

### その他
### json-serverの実行コマンド（json-serverをバックエンドとして使用する場合、.envのURLを書き換える必要がある）
```bash
json-server --watch db.json
```