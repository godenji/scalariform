package scalariform.lexer

object NoHiddenTokens extends HiddenTokens(Nil)

case class HiddenTokens(tokens: List[HiddenToken]) extends Iterable[HiddenToken] {

  def removeInitialWhitespace = HiddenTokens(tokens.dropWhile(_.isInstanceOf[Whitespace]))

  override def iterator: Iterator[HiddenToken] = tokens.iterator

  val comments: List[Comment] = tokens collect { case comment: Comment ⇒ comment }

  val scalaDocComments: List[ScalaDocComment] = tokens collect { case comment @ ScalaDocComment(_) ⇒ comment }

  val whitespaces: List[Whitespace] = tokens collect { case whitespace @ Whitespace(_) ⇒ whitespace }

  def firstTokenOption: Option[HiddenToken] = tokens.headOption

  def lastTokenOption: Option[HiddenToken] = tokens.lastOption

  def containsNewline: Boolean = text contains '\n'

  def containsComment: Boolean = comments.nonEmpty

  def containsUnicodeEscape: Boolean = {
    for (token ← tokens if token.token.containsUnicodeEscape)
      return true
    false
  }

  lazy val text: String = {
    val sb = new StringBuilder
    for (token ← tokens) sb.append(token.text)
    sb.toString
  }
  lazy val rawText: String = tokens.map(_.token.rawText).mkString

  def rawTokens: List[Token] = tokens.map(_.token)

  def offset: Int = tokens.head.token.offset

  def lastCharacterOffset: Int = tokens.last.token.lastCharacterOffset

}
