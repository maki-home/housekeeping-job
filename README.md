Run one-off task

```
cf run-task housekeeping-job "$(cf curl /v3/apps/$(cf app housekeeping-job --guid)/droplets/current | jq -r .process_types.task)"
```

Register cron scheduler

```
cf create-job housekeeping-job housekeeping-cron "$(cf curl /v3/apps/$(cf app housekeeping-job --guid)/droplets/current | jq -r .process_types.task)"
cf schedule-job housekeeping-cron "0 23 ? * *"
```