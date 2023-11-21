package ytstorage

import ytstorage.lib.qrmedia.QRMedia
import java.awt.image.BufferedImage


object Main:
  val help = """
--------------------------
~HELP~

Commands:
  run encode <file> <target.mp4>
  run encode <folder> <target.mp4>
  run decode <video.mp4>

--------------------------
  """.stripMargin

  def main(args: Array[String]) =
    println(help)

    var i = 0
    for arg <- args do
      arg.toLowerCase match
        case "encode" =>
          require(i + 1 < args.length, "You used 'encode' wrong!")
          i += 1

          val frames: Frames = QRMedia.from(path=args(i), version=40)
            .encode(width=1920, height=1080, scale=2)
          frames.exportAsMOV(if i + 1 >= args.length then "video.mp4" else { i += 1; args(i) }, 60)
          
          // for j <- 0 until frames.frames.length do
          //  Files.store(frames(j), s"frame$j.jpg")
          
        case "decode" =>
          require(i + 1 < args.length, "You used 'decode' wrong!")
          i += 1

        case _ => ;

      i += 1
