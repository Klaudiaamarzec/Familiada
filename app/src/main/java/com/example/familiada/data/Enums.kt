enum class Team(val displayName: String) {
    FIRST("Drużyna 1"),
    SECOND("Drużyna 2");

    override fun toString(): String {
        return displayName
    }
}
