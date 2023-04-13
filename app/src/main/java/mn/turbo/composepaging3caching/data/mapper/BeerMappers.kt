package mn.turbo.composepaging3caching.data.mapper

import mn.turbo.composepaging3caching.data.local.BeerEntity
import mn.turbo.composepaging3caching.data.remote.BeerDto
import mn.turbo.composepaging3caching.domain.Beer

fun BeerDto.toBeerEntity(): BeerEntity {
    return BeerEntity(
        id = id,
        name = name,
        tagline = tagline,
        description = description,
        firstBrewed = first_brewed,
        imageUrl = image_url,
    )
}

fun BeerEntity.toBeer(): Beer {
    return Beer(
        id = id,
        name = name,
        tagline = tagline,
        desc = description,
        firstBrewed = firstBrewed,
        imageUrl = imageUrl
    )
}
