import pandas as pd
import datetime as dt

def parse_datetime(date_time_series, format=None):
	# Source - https://stackoverflow.com/a/29882676
	# Posted by fixxxer, modified by community. See post 'Timeline' for change history
	# Retrieved 2026-03-13, License - CC BY-SA 4.0
    """
    This is an extremely fast approach to datetime parsing.
    For large data, the same dates are often repeated. Rather than
    re-parse these, we store all unique dates, parse them, and
    use a lookup to convert all dates.
    """
    dates = {value:pd.to_datetime(value, errors='coerce', format=format) for value in date_time_series.unique()}
    return date_time_series.map(dates)
    
column_types = {
    'date': str,
    'time': str,
    'spressure': 'Int64',
    'dpressure': 'Int64',
    'rate': 'Int64',
    'arrhythmia': str,
    'notes': 'string'
}

# Load the CSV into a DataFrame
df = pd.read_csv('measurements.csv', dtype=column_types)

# Preview the first few rows of the raw data
print("Initial data:\n", df.head())

#Check the dataframe for missing values
print(df.isna().sum())

#Filter out rows without useful information
df = df[(~df.spressure.isnull()) & (~df.dpressure.isnull())]

print(df.isna().sum())

#Fill in other missing data with some reasonable values
df['time'] = df['time'].fillna('0.0')
df['arrhythmia'] = df['arrhythmia'].fillna('?')

#Combine the date and time columns into a new column with appropriate timestamp formatting
df['datetime'] = parse_datetime(df['date'] + ' ' + df['time'], format='%d.%m.%y %H.%M')

adjusted_data = df[['datetime', 'spressure', 'dpressure', 'rate',  'arrhythmia', 'notes']]

# Preview the first few rows
print("Adjusted data:\n", adjusted_data.head())

#Save the adjusted data
adjusted_data.to_csv('adjusted_measurements.csv', index=False, date_format='%d-%m-%Y %H:%M')

#Check the saved data
saved_data = pd.read_csv('adjusted_measurements.csv', dtype=column_types)
print("Saved data:\n", saved_data.head(15))


