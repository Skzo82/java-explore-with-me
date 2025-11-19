ALTER TABLE events ADD COLUMN IF NOT EXISTS lat DOUBLE PRECISION;
ALTER TABLE events ADD COLUMN IF NOT EXISTS lon DOUBLE PRECISION;

DO $$
BEGIN
  IF EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_name = 'events' AND column_name = 'location_lat'
  ) THEN
    EXECUTE 'UPDATE events SET lat = COALESCE(lat, location_lat)';
  END IF;

  IF EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_name = 'events' AND column_name = 'location_lon'
  ) THEN
    EXECUTE 'UPDATE events SET lon = COALESCE(lon, location_lon)';
  END IF;
END
$$;

DO $$
BEGIN
  IF EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_name = 'events' AND column_name = 'location_lat'
  ) THEN
    EXECUTE 'ALTER TABLE events DROP COLUMN location_lat';
  END IF;

  IF EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_name = 'events' AND column_name = 'location_lon'
  ) THEN
    EXECUTE 'ALTER TABLE events DROP COLUMN location_lon';
  END IF;
END
$$;