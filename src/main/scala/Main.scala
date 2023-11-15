package ytstorage

import ytstorage.Files.toLines


object Main:
  val help = """
--------------------------
~HELP~

Commands:
  run encode <file.txt>
  run encode <folder>
  run decode <video.mp4>

--------------------------
  """.stripMargin

  def main(args: Array[String]) =
    println(help)

    var i = 0
    for arg <- args do
      val arg = args(i)
      arg.toLowerCase match
        case "encode" =>
          require(i + 1 < args.length, "You used 'encode' wrong!")
          i += 1

          val encoder = new Encoder()
          
          val file = Files.read(path = args(i))
          val image = encoder.encode(file.toLines)
          Files.store(image, "resources/image")

        case "decode" =>
          require(i + 1 < args.length, "You used 'decode' wrong!")

        case _ => ;
      
      i += 1
