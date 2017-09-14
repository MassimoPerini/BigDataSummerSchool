package bigdata.ml

import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.rdd.RDD



object App {

  
  def distanceSquare(p1: (Double, Double), p2: (Double, Double)): Double = {
    Math.pow(p1._1-p2._1, 2)+Math.pow(p1._2-p2._2, 2)
  }

  def findNearestCenter(centers: Array[(Double, Double)], p: (Double, Double)): (Double, Int) = {
    val distanceWithIndex = centers.zipWithIndex.map {
      case (c, i) => (distanceSquare(c, p), i)
    }
    var res=distanceWithIndex.minBy(_._1)
    
    val best_distance = res._1
    val best_center_id = res._2
    (best_distance, best_center_id.toInt)
  }

  def main(args: Array[String]) {
    println("Arguments = " + args.mkString(" "))

    val infile = args(0) // path of order file
    val K = args(1).toInt // num of centers
    val N = args(2).toInt // max iterations
    val outfile = args(3) // file path  of final center points
    val sparkConf = new SparkConf().setAppName("KMeans")
    val sc = new SparkContext(sparkConf)

    val input = sc.textFile(infile)
    val points = input.map (
      line =>
        {
          val arr = line.split(",")
          (arr(3).toDouble, arr(4).toDouble)
        }
    ).cache()
    
    val minx = points.map(p => p._1).min()
    val maxx = points.map(p => p._1).max()
    val miny = points.map(p => p._2).min()
    val maxy = points.map(p => p._2).max()
        val r = scala.util.Random

    val KMeansKernel: ((Array[(Double, Double)], Double, Boolean), Int) => (Array[(Double, Double)], Double, Boolean) = {
      case ((centers, previousDistance, isEnd), iter) =>
        {

          if (isEnd) {
            (centers, previousDistance, isEnd)
            
          } else {
               val rslt = points.map(
                p => {
                  var tmp=findNearestCenter(centers, p)
                  (tmp._2, (p, tmp._1))
                })
                       
           type ScoreCollector = (Double, Double, Double, Int)
                        
            val createFirstCombiner = (v:((Double, Double),Double)) => (v._1._1, v._1._2, v._2, 1)
            val createCombiner = (acc:ScoreCollector , v:((Double, Double),Double)) =>(acc._1+v._1._1,acc._2+v._1._2,acc._3+v._2,acc._4+ 1)
            val createMerger = (acc:ScoreCollector , v:ScoreCollector) => (acc._1+v._1, acc._2+v._2, acc._3+v._3, acc._4+v._4)
            val sumCount = rslt.combineByKey(createFirstCombiner, createCombiner, createMerger)
            //NewCenters ---- 
            var newCenters=sumCount.map{
              p =>  (p._2._1/p._2._4, p._2._2/p._2._4)
            }.collect()
            
            if (newCenters.size<K)
            {
              val arrTmp: Array[(Double, Double)]=new Array[(Double, Double)](K-newCenters.size).map(
                        p => (((r.nextDouble()*(maxx-minx))+minx), (r.nextDouble()*(maxy-miny))+miny)
                  )
                  newCenters = newCenters.++:(arrTmp)
            }
            // ---------- SumDistance
            val tmpTot= sumCount.reduce{
              (a,b) => (0,(0,0,a._2._3+b._2._3,0))
            }
            
            val sumDistance:Double=tmpTot._2._3
            // -----------
            println("iter = " + iter.toString() + "\tsum distance = " + sumDistance.toString())

            (newCenters, sumDistance, previousDistance == sumDistance)
            
          }
        }
    }

    
    var randomCenters : Array[(Double, Double)] = new Array[(Double, Double)](K)
    randomCenters=randomCenters.map
    {
      p => (((r.nextDouble()*(maxx-minx))+minx), (r.nextDouble()*(maxy-miny))+miny)
    }
    
    val (finalCenters, sumDistance, isEnd) = Range(0, N).foldLeft((randomCenters, Double.MaxValue, false))(KMeansKernel)
    
    val rslt = points.map{
                p => 
                  var tmp=findNearestCenter(finalCenters, p)
                  (tmp._2, 1)
    }
           val tmp2=(rslt.reduceByKey((a,b) => (a+b)))
           val s=tmp2.map{
             p => "cluster: "+p._1+", points inside: "+p._2+", center location: "
           }

    sc.parallelize(s.collect() zip finalCenters).saveAsTextFile(outfile)
  }

}
