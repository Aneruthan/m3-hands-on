public record UserDTO(
    long id,
    String name,
    String email,
    boolean active
) {
    public static UserDTO fromUser(User user) {
        return new UserDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.isActive()
        );
    }

    public static void main(String[] args) {
        User user = new User(1, "Alice", "alice@example.com", true);

        UserDTO dto = fromUser(user);

        System.out.println(dto);
    }
}