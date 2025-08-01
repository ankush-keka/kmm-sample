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
            name: "shared",
            url: "https://github.com/ankush-keka/kmm-sample/releases/download/0.0.2-snapshot/shared.xcframework.zip",
            checksum: "905e3130c425db89da231195f74b03cbfe5325ac60da50b4162db40e5626e01f"
        ),
    ]
)
