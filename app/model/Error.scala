package model

sealed trait Error

case object ServiceUnavailable extends Error