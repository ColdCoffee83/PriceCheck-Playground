{ pkgs }: {
    deps = [
        pkgs.adoptopenjdk-openj9-bin-11
        pkgs.@Service
        pkgs.graalvm17-ce
        pkgs.maven
        pkgs.replitPackages.jdt-language-server
        pkgs.replitPackages.java-debug
    ];
}