// swift-tools-version:5.3
import PackageDescription

let package = Package(
    name: "KmmSharedSpm",
    platforms: [
        .iOS(.v13)
    ],
    products: [
        .library(
            name: "KmmSharedSpm",
            targets: ["KmmSharedSpm"]),
    ],
    dependencies: [
        // no dependencies
    ],
    targets: [
        .binaryTarget(
            name: "KmmSharedSpm",
            url: "https://github.com/ankush-keka/kmm-sample/releases/download/0.0.4-snapshot/KmmSharedSpm.xcframework.zip",
            checksum: "5d96e265c6d96a467e09730a59a4b620dd650dfe9256b1b9f0e009df7461e1a8"
        ),
    ]
)
