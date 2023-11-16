package ytstorage.lib.qrmedia

import java.awt.image.BufferedImage
import scala.util.Try


case class QRMedia():
	def encode(width: Int = 512, height: Int = 512): Vector[BufferedImage] = ???




object QRMedia:
	def from(path: String, version: Int): QRMedia =
		require(version > 0 && version <= 40,
			s"Version: $version. 'version' is required to be in range (0, 40].")
		
		new QRMedia()