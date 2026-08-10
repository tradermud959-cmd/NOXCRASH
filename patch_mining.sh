sed -i '/setMiningStatus(MiningStatus.COMPLETED)/a \
                NoxNotificationManager.addNotification(\
                    NoxNotificationType.MINER,\
                    "MINER SELESAI",\
                    "Proses mining telah mencapai waktunya. Cek detail aktivitasmu."\
                )' app/src/main/java/com/example/MiningManager.kt
