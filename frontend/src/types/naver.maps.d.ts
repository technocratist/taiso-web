// Minimal typings for Naver Maps API
declare namespace naver {
  namespace maps {
    class Map {
      constructor(container: HTMLElement, options: any);
      fitBounds(bounds: LatLngBounds): void;
      // add other methods or properties as needed
    }

    class LatLng {
      constructor(lat: number, lng: number);
    }

    class Polyline {
      constructor(options: any);
    }

    class LatLngBounds {
      extend(latLng: LatLng): void;
    }
  }
}
