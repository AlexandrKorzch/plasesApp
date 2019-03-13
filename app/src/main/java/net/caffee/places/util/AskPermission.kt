package net.caffee.places.util

class AskPermission(
        val function: (Boolean) -> Unit,
        val permissions: Array<String>) {
}
