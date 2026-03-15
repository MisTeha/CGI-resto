# CGI-resto Agent Plan

## MINU PROMPT:

this is a landing page for a restaurant booking system. it has little design (just whats needed to make it nice) start off with a basic skeleton with a search filter for date, time, restaurant zone (room) and num of people, then the search gives out a list of tables with coordinates. The frontend app needs to put the tables visibly on a grid (with clickable table objects) and also next to the grid should be a table properties window (with other available times and just some properties). i dont want the whole thing rn, more to plan out what is needed and start from the skeleton.
#file:test.http backend's endpoints.

lisaks palusin agendil valida component frameworki. Ta otsustas DaisyUI ja lisaks shadcn kasuks.


## Goal

Build the first frontend skeleton for a restaurant booking system landing page in SvelteKit.

This iteration should focus on structure, API wiring, and a usable interaction model rather than a full polished product.

## Current decisions

- Keep the solution fairly simple.
- Support mobile from the start.
- Use **DaisyUI** for quick UI primitives.
- Use **shadcn-svelte** for higher-level interactive pieces when useful.
- Keep styling light and practical.
- Use backend-driven table coordinates instead of hardcoded frontend positions.
- Fetch zones dynamically from the backend.
- Use 15-minute interval time selection.

## Expected user flow

1. User opens the landing page.
2. User sees a search/filter bar with:
	 - date
	 - time
	 - duration
	 - zone / room
	 - number of people
	 - optional preferences like window/privacy
3. User submits search.
4. Frontend requests availability recommendations from backend.
5. Matching tables are shown on a visible floor-plan grid.
6. User clicks a table.
7. A side panel opens with table properties and selectable times.
8. User may keep the same time or change it.
9. User enters booking details and submits.
10. Frontend sends booking request to backend.

## Backend work needed

### 1. Add table coordinates

Update `RestoTable` to include:

- `gridX: Integer`
- `gridY: Integer`

These coordinates define where the table appears on the floor-plan grid.

### 2. Seed coordinates

Update `DataLoader` so seeded tables have sensible `gridX` / `gridY` values.

### 3. Expose coordinates in API responses

Update table and availability response mapping so these are returned:

- `gridX`
- `gridY`

### 4. Add zones endpoint

Add `GET /api/zones`.

Suggested response shape:

```json
[
	{
		"id": 1,
		"name": "Main Hall",
		"description": "..."
	}
]
```

## Frontend work needed

### 1. Shared types and API helpers

Create shared frontend types for:

- zone
- table recommendation
- booking payload
- search params

Create lightweight fetch helpers for:

- fetching zones
- fetching recommendations
- posting bookings

### 2. Search/filter skeleton

Create a top search bar with:

- date input
- time dropdown in 15-minute increments
- duration dropdown
- zone dropdown
- party size input
- preference toggles
- search button

### 3. Floor-plan grid skeleton

Create a grid area that visually places tables based on `gridX` and `gridY`.

Tables should be clickable objects, not plain text rows.

Recommended initial states:

- available
- selected
- recommended
- unavailable / not suitable

### 4. Table details panel

Create a panel that shows when a table is selected.

It should include:

- table number
- zone
- seat count
- window flag
- privacy score
- available or selectable times
- customer name input
- customer phone input
- booking button

The panel should allow time adjustment before booking submission.

### 5. Page layout

For desktop:

- search bar on top
- floor-plan grid on the left
- details panel on the right

For mobile:

- search bar stacked vertically
- grid full width
- details panel becomes a bottom sheet / drawer after table tap

## Mobile notes

- Use responsive stacking from the beginning.
- Keep form controls easy to tap.
- Let the grid scroll horizontally/vertically if needed.
- Reduce visual density on smaller screens.
- Prefer a bottom drawer for table details instead of a fixed sidebar.

## Suggested component structure

- `src/lib/components/SearchBar.svelte`
- `src/lib/components/FloorGrid.svelte`
- `src/lib/components/TablePanel.svelte`
- `src/lib/api.ts`
- `src/lib/index.ts` for shared types/utilities
- `src/routes/+page.svelte` to compose the page

## Suggested implementation order

1. Backend coordinates and zones endpoint
2. Frontend types and API helpers
3. Search bar skeleton
4. Grid skeleton with clickable tables
5. Side panel skeleton
6. Mobile drawer behavior
7. Booking submission wiring

## Non-goals for this iteration

- Full restaurant design system
- Advanced animations
- Complex drag-and-drop table interactions
- Full booking management UI
- Admin tooling

## Notes for future agents

- Prefer small, focused changes.
- Avoid overengineering.
- Keep the UI readable and demo-ready.
- Favor existing backend endpoints and simple fetch logic.
- If adding DaisyUI or shadcn-svelte, keep setup minimal and aligned with the current Tailwind/SvelteKit stack.
