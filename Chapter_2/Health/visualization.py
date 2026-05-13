import matplotlib.pyplot as plt
import pandas as pd

column_types = {
    'datetime': 'string',
    'spressure': 'Int64',
    'dpressure': 'Int64',
    'rate': 'Int64',
    'arrhythmia': 'string',
    'notes': 'string'
}

df = pd.read_csv('adjusted_measurements.csv', dtype=column_types)

data_time = df['datetime'] = pd.to_datetime(df['datetime'], format='%d-%m-%Y %H:%M')

correlation = df['spressure'].corr(df['dpressure'])
print("Pearson correlation between SP and DP:", correlation)

fig = plt.figure(figsize=(20, 9))
ax = fig.add_subplot(2,1,1)

plt.plot(df['datetime'], df['spressure'], color='orange', alpha=0.7)
plt.plot(df['datetime'], df['dpressure'], color='navy', alpha=0.7)

normal_rate = df[df['arrhythmia'] == 'N']
arrhythmic_rate = df[df['arrhythmia'] != 'N']

plt.scatter(normal_rate['datetime'], normal_rate['rate'], color="green")
plt.scatter(arrhythmic_rate['datetime'], arrhythmic_rate['rate'], color="red")

plt.title('Pressure and heart beat rate')
plt.xlabel('Date')
plt.ylabel('Systolic/diastolic pressure & rate')
ax.legend(['Systolic pressure', 'Diastolic pressure', 'Normal beat rate', 'Arrhythmic beat rate'], loc="upper center", bbox_to_anchor=(0.5, 1.02), ncol=4)
plt.tight_layout()
plt.grid(True)

ax = fig.add_subplot(2,1,2)
ax.text(0, 0.9, "Pearson (linear) correlation between SP and DP: " + str(correlation))
ax.axis("off")
plt.grid(False)

#Save the plot on the disk
plt.savefig('output/health_plot.png')

plt.show()
