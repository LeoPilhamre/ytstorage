package ytstorage.lib.qrmedia

import java.awt.image.BufferedImage
import ytstorage.Files
import java.io.File
import com.google.zxing.qrcode.encoder.{QRCode, Encoder}
import scala.collection.mutable.ArrayBuffer
import ytstorage.Frames
import ytstorage.Frames.*
import com.google.zxing.qrcode.decoder.*

case class QRMedia(file: File, version: Int):
	import QRMedia.*

	/**
	  * Encode `file` ... TODO
	  *
	  * @param width
	  * @param height
	  * @param alignment
	  * @param allowMultiple
	  * @return
	  */
	def encode(
		width: Int = 512,
		height: Int = 512,
		alignment: Alignment = Alignment.Cover,
		allowMultiple: Boolean = true
	): Frames =
		val frames: ArrayBuffer[BufferedImage] = ArrayBuffer[BufferedImage]()
		
		//TODO: Alignment Cover
		
		var content = ""
		val code: QRCode = Encoder.encode(content, ErrorCorrectionLevel.L)
		code.setVersion(Version.getVersionForNumber(40))
		code.setMode(Mode.)
		
		frames.toSeq.toFrames
		

object QRMedia:
	enum Alignment:
		case Cover, Fit, Center

	def from(path: String, version: Int = 16): QRMedia =
		require(version > 0 && version <= 40,
			s"Version: $version. 'version' is required to be in range (0, 40].")

		val file = Files.read(path)
		new QRMedia(file, version)