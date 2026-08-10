sed -i '/updateHistoryStatus(startedAt, "SELESAI")/a \
                NoxNotificationManager.addNotification(\
                    NoxNotificationType.AI_MODE,\
                    "AI MODE SELESAI",\
                    "AI Mode telah menyelesaikan proses otomatisasinya. Cek hasil aktivitasmu."\
                )' app/src/main/java/com/example/AIManager.kt
