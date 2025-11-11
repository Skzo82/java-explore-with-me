ALTER TABLE events
  ALTER COLUMN location_lat TYPE double precision USING location_lat::double precision,
  ALTER COLUMN location_lon TYPE double precision USING location_lon::double precision;