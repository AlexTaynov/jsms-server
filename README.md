## Запуск постгреса в докере и создание БД.

```bash
docker compose up -d
```

```bash
docker exec -it jsms-server-postgres-1 psql -U postgres -c "CREATE DATABASE jsms_db"
```

## Запуск приложения
* Создать файл application.yml в папке [config](./config)
* Скопировать содержимое [application.template.yml](./config/application.template.yml) в файл application.yml
* В нем должны храниться данные подключения к БД для локального запуска

