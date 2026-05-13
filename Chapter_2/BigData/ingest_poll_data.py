##import required libraries
import pyspark.sql

##create spark session
spark = pyspark.sql.SparkSession \
        .builder \
        .appName("Python Spark SQL basic example") \
        .config('spark.driver.extraClassPath', "postgresql-42.7.8.jar") \
        .getOrCreate()

##read movies table from db using spark
def extract_collection_items_to_df():
    items_df = spark.read \
        .format("jdbc") \
        .option("url", "jdbc:postgresql://localhost:5432/collection") \
        .option("dbtable", "collection.items") \
        .option("user", "collector") \
        .option("password", "development") \
        .option("driver", "org.postgresql.Driver") \
        .load()
    return items_df

def extract_poll_data_from_csv(fileName):
    df = spark.read.option("header",True) \
          .option("inferSchema", True) \
          .csv(fileName)
    #df.printSchema()
    #df.show()
    poll_view = df.createOrReplaceTempView("poll_data")
    poll_data_df = spark.sql("SELECT * FROM poll_data")
    poll_data_df.show()
    return poll_data_df

def transform_avg_ratings(items_df, ratings_df):
    ## joining items with ratings
    avg_rating = ratings_df.groupBy("item_id").mean("rating")
    df = items_df.join(
    avg_rating,
    items_df.id == avg_rating.item_id
    )
    df = df.drop("item_id")
    df = df.drop("image")
    df = df.drop("small_image")
    return df

##load transformed dataframe to the database
def load_item_ratings_to_db(df):
    mode = "overwrite"
    url = "jdbc:postgresql://localhost:5432/collection"
    properties = {"user": "collector",
                  "password": "development",
                  "driver": "org.postgresql.Driver"
                  }
    df.write.jdbc(url=url,
                  table = "collection.item_ratings",
                  mode = mode,
                  properties = properties)


if __name__ == "__main__":
    items_df = extract_collection_items_to_df()
    poll_data_df = extract_poll_data_from_csv("poll_data.csv")
    ratings_df = transform_avg_ratings(items_df, poll_data_df)
    load_item_ratings_to_db(ratings_df)
