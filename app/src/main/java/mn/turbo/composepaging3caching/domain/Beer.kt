package mn.turbo.composepaging3caching.domain

data class Beer(
    val id: Int,
    val name: String,
    val tagline: String,
    val firstBrewed: String,
    val desc: String,
    val imageUrl: String?
)
